package com.seeder;

import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.entity.Role;
import com.enums.RoleEnum;
import com.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent>{

	//@Autowired  = 1️ Do NOT Use @Autowired With @RequiredArgsConstructor
	//This is wrong usage. 
	//@RequiredArgsConstructor already creates constructor injection.
	private final RoleRepository rr;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		log.debug("Roles Added: "+ event);
		log.info("Role seeding process started");
		loadRoles();
		
	}
	
	private void loadRoles() {
		
		Map<RoleEnum, String> roleDescription = Map.of(
		        RoleEnum.USER, "Default User Role",
		        RoleEnum.ADMIN, "Administrator Role"
		);

		
		//Java 11 Style
		for (RoleEnum roleEnum : RoleEnum.values()) {

            if (rr.findByRolename(roleEnum).isEmpty()) {

                Role role = new Role();
                role.setRolename(roleEnum);
                role.setDescription(roleDescription.get(roleEnum));

                rr.save(role);

                log.info("Role created: {}", roleEnum);
            }
            
		}
	}

}
