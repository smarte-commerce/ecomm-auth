package com.winnguyen1905.auth.config;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.winnguyen1905.auth.persistance.entity.EPermission;
import com.winnguyen1905.auth.persistance.entity.ERole;
import com.winnguyen1905.auth.persistance.entity.EVendor;
import com.winnguyen1905.auth.common.constant.AccountType;
import com.winnguyen1905.auth.persistance.entity.EAccountCredentials;
import com.winnguyen1905.auth.persistance.entity.ECustomer;
import com.winnguyen1905.auth.persistance.repository.PermissionRepository;
import com.winnguyen1905.auth.persistance.repository.RoleRepository;
import com.winnguyen1905.auth.persistance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

  // private final PermissionRepository permissionRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  // private final RoleRepository roleRepository;

  // @Transactional
  @Override
  public void run(String... args) throws Exception {

    // // location
    // final DistrictEntity district = new DistrictEntity();
    // district.setCode("thu-duc");
    // district.setName("Thu Duc");
    // district.setCreatedBy("ADMINSTRATOR");
    // final CityEntity city = new CityEntity();
    // city.setName("Ho Chi Minh");
    // city.setCode("ho-chi-minh");
    // district.setCity(city);
    // districtRepository.save(district);
    // permission]

    // EPermission permission = new EPermission();
    // permission.setApiPath("/api/v1/");
    // permission.setCode("admin");
    // permission.setName("Admin full permission");
    // permission.setMethod("GET");

    // final PermissionEntity permission2 = new PermissionEntity();
    // permission.setApiPath("/api/v1/buildings");
    // permission.setCode("building");
    // permission.setName("create building");
    // permission.setMethod("POST");

    // ERole role = new ERole();
    // role.setCode("admin");
    // role.setName("ADMIN");
    // role.getPermissions().add(permission);
    // permission.getRoles().add(role);

    // this.roleRepository.save(role);
    // this.permissionRepository.save(permission);
    // // User
    EAccountCredentials user = EAccountCredentials.builder()
        .username("1")
        .password(this.passwordEncoder.encode("1"))
        .status(true)
        .accountType(AccountType.CUSTOMER)
        .id(UUID.fromString("11111111-1111-4111-8111-111111111111"))
        .build();

    ECustomer customer = ECustomer.builder()
        .accountCredentials(user)
        .customerName("Duy Nguyen")
        .customerAddress("123 Main St")
        .customerPhone("1234567890")
        .customerEmail("loikhung2k4@gmail.com")
        .customerLogo("https://example.com/logo.png")
        .customerStatus(true)
        .build();

    EVendor vendor = EVendor.builder()
        .accountCredentials(user)
        .vendorName("Duy Nguyen")
        .vendorAddress("123 Main St")
        .vendorPhone("1234567890")
        .vendorEmail("loikhung2k4@gmail.com")
        .vendorLogo("https://example.com/logo.png")
        .vendorStatus(true)
        .build();

    user.setVendor(vendor);
    user.setCustomer(customer);
    this.userRepository.save(user);
  }
}
