package org.example.market.service;

import lombok.RequiredArgsConstructor;
import org.example.market.entity.Role;
import org.example.market.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findUserRole();
    }
}
