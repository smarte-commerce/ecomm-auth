package com.winnguyen1905.gateway.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winnguyen1905.gateway.persistance.entity.EPermission;
import com.winnguyen1905.gateway.persistance.entity.ERole;
import com.winnguyen1905.gateway.persistance.repository.PermissionRepository;
import com.winnguyen1905.gateway.persistance.repository.RoleRepository;
import com.winnguyen1905.gateway.persistance.repository.UserRepository;

@Service
public class DatabaseInitializer implements CommandLineRunner {

  private final PermissionRepository permissionRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  public DatabaseInitializer(PermissionRepository permissionRepository,
      UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
    this.permissionRepository = permissionRepository;
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // @Transactional
  @Override
  public void run(String... args) throws Exception {

    if (!this.userRepository.findAll().isEmpty()) return;

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

    // permission
    EPermission permission = new EPermission();
    permission.setApiPath("/api/v1/");
    permission.setCode("admin");
    permission.setName("Admin full permission");
    permission.setMethod("GET");

    // final PermissionEntity permission2 = new PermissionEntity();
    // permission.setApiPath("/api/v1/buildings");
    // permission.setCode("building");
    // permission.setName("create building");
    // permission.setMethod("POST");

    ERole role = new ERole();
    role.setCode("admin");
    role.setName("ADMIN");
    // role.setPermissions(Set.of(permission));
    // permission.setRoles(Set.of(role));

    this.roleRepository.save(role);
    this.permissionRepository.save(permission);
    // // User
    // EUserCredentials user = new EUserCredentials();
    // user.setUsername("loikhung2k4");
    // user.setPassword(this.passwordEncoder.encode("12345678"));
    // // user.setRole(role);
    // user.setType("customer");
    // user.setId(UUID.fromString("16d3ffd5-1537-4b53-a031-ce6f71fcd06c"));
    // this.userRepository.save(user);
  }

}
