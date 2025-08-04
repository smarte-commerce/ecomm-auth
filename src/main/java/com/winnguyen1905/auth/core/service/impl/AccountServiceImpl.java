package com.winnguyen1905.auth.core.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winnguyen1905.auth.core.model.response.AccountVm;
import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.service.AccountServiceInterface;
import com.winnguyen1905.auth.exception.ResourceAlreadyExistsException;
import com.winnguyen1905.auth.exception.ResourceNotFoundException;
import com.winnguyen1905.auth.persistance.entity.EAccountCredentials;
import com.winnguyen1905.auth.persistance.entity.ECustomer;
import com.winnguyen1905.auth.persistance.entity.EVendor;
import com.winnguyen1905.auth.persistance.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountServiceInterface {
  
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  public Mono<AccountVm> getUserByUsername(String username) {
    return Mono.fromCallable(() -> this.userRepository.findUserByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Not found user by username " + username)))
        .map(user -> this.modelMapper.map(user, AccountVm.class))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<AccountVm> getUserById(UUID id) {
    return Mono.fromCallable(() -> this.userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id)))
        .map(user -> this.modelMapper.map(user, AccountVm.class))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<AccountVm> createUser(AccountVm accountVm) {
    return Mono.fromCallable(() -> {
      if (this.userRepository.findUserByUsername(accountVm.username()).isPresent()) {
        throw new ResourceAlreadyExistsException("Username already exists: " + accountVm.username());
      }
      
      EAccountCredentials account = new EAccountCredentials();
      account.setUsername(accountVm.username());
      account.setPassword(accountVm.password()); // Should be encoded
      account.setStatus(true);
      account.setAccountType(accountVm.accountType());
      
      return this.userRepository.save(account);
    })
    .map(account -> this.modelMapper.map(account, AccountVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<AccountVm> updateUser(UUID id, AccountVm accountVm) {
    return Mono.fromCallable(() -> {
      EAccountCredentials account = this.userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
      
      if (accountVm.username() != null && !accountVm.username().equals(account.getUsername())) {
        if (this.userRepository.findUserByUsername(accountVm.username()).isPresent()) {
          throw new ResourceAlreadyExistsException("Username already exists: " + accountVm.username());
        }
        account.setUsername(accountVm.username());
      }
      
      if (accountVm.password() != null) {
        account.setPassword(accountVm.password()); // Should be encoded
      }
      
      if (accountVm.accountType() != null) {
        account.setAccountType(accountVm.accountType());
      }
      
      return this.userRepository.save(account);
    })
    .map(account -> this.modelMapper.map(account, AccountVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  @Transactional
  public Mono<Void> deleteUser(UUID id) {
    return Mono.fromRunnable(() -> {
      EAccountCredentials account = this.userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
      
      this.userRepository.delete(account);
    })
    .subscribeOn(Schedulers.boundedElastic())
    .then();
  }

  @Override
  @Transactional
  public Mono<Void> deleteUsers(List<UUID> ids) {
    return Mono.fromRunnable(() -> {
      List<EAccountCredentials> accounts = this.userRepository.findAllById(ids);
      this.userRepository.deleteAll(accounts);
    })
    .subscribeOn(Schedulers.boundedElastic())
    .then();
  }

  @Override
  public Mono<PagedResponse<AccountVm>> getAllUsers(int page, int size, String sortBy, String sortDir) {
    return Mono.fromCallable(() -> {
      Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) 
        ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
      
      Pageable pageable = PageRequest.of(page, size, sort);
      Page<EAccountCredentials> accountsPage = this.userRepository.findAll(pageable);
      
      List<AccountVm> content = accountsPage.getContent().stream()
        .map(account -> this.modelMapper.map(account, AccountVm.class))
        .collect(Collectors.toList());
      
      return new PagedResponse<>(
        content,
        accountsPage.getNumber(),
        accountsPage.getSize(),
        accountsPage.getTotalElements(),
        accountsPage.getTotalPages(),
        accountsPage.isLast()
      );
    })
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<PagedResponse<AccountVm>> searchUsers(String keyword, int page, int size) {
    return Mono.fromCallable(() -> {
      Pageable pageable = PageRequest.of(page, size);
      Page<EAccountCredentials> accountsPage = this.userRepository.findByUsernameContainingIgnoreCase(keyword, pageable);
      
      List<AccountVm> content = accountsPage.getContent().stream()
        .map(account -> this.modelMapper.map(account, AccountVm.class))
        .collect(Collectors.toList());
      
      return new PagedResponse<>(
        content,
        accountsPage.getNumber(),
        accountsPage.getSize(),
        accountsPage.getTotalElements(),
        accountsPage.getTotalPages(),
        accountsPage.isLast()
      );
    })
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<AccountVm> activateUser(UUID id) {
    return Mono.fromCallable(() -> {
      EAccountCredentials account = this.userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
      
      account.setStatus(true);
      return this.userRepository.save(account);
    })
    .map(account -> this.modelMapper.map(account, AccountVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<AccountVm> deactivateUser(UUID id) {
    return Mono.fromCallable(() -> {
      EAccountCredentials account = this.userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
      
      account.setStatus(false);
      return this.userRepository.save(account);
    })
    .map(account -> this.modelMapper.map(account, AccountVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  @Transactional
  public Mono<AccountVm> assignCustomerToAccount(UUID accountId, UUID customerId) {
    return Mono.fromCallable(() -> {
      // In a real implementation, this would fetch the customer and assign it to the account
      // This is a simplified implementation
      EAccountCredentials account = this.userRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + accountId));
      
      ECustomer customer = new ECustomer();
      customer.setId(customerId);
      customer.setAccountCredentials(account);
      account.setCustomer(customer);
      
      return this.userRepository.save(account);
    })
    .map(account -> this.modelMapper.map(account, AccountVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  @Transactional
  public Mono<AccountVm> assignVendorToAccount(UUID accountId, UUID vendorId) {
    return Mono.fromCallable(() -> {
      // In a real implementation, this would fetch the vendor and assign it to the account
      // This is a simplified implementation
      EAccountCredentials account = this.userRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + accountId));
      
      EVendor vendor = new EVendor();
      vendor.setId(vendorId);
      vendor.setAccountCredentials(account);
      account.setVendor(vendor);
      
      return this.userRepository.save(account);
    })
    .map(account -> this.modelMapper.map(account, AccountVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }
}
