package com.example.spring_authentication.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import com.example.spring_authentication.domain.enums.Role;
import com.example.spring_authentication.domain.validation.GroupValidation;

@Table(name = "usuario")
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "id_usuario")
    private String id;

    @NotBlank(groups = GroupValidation.Create.class)
    @Size(max = 255)
    @Column(name = "nome", length = 255, nullable = false)
    private String name;

    @NotBlank(groups = {GroupValidation.Create.class, GroupValidation.Login.class})
    @Size(max = 255)
    @Email
    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;
    
    @NotBlank(groups = {GroupValidation.Create.class, GroupValidation.Login.class})
    @Size(max = 255, min = 6)
    @Column(name = "senha", length = 255, nullable = false)
    private String password;

    @Column(name = "nivel", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "valido", nullable = false)
    private Boolean valid;
    
    @Column(name = "valido_por_admin", nullable = false)
    private Boolean validByAdmin;

    @NotNull(groups = GroupValidation.Create.class)
    @Column(name = "criado", nullable = false)
    private LocalDateTime created = LocalDateTime.now();
    
    @NotNull(groups = GroupValidation.Create.class)
    @Column(name = "alterado", nullable = false)
    private LocalDateTime updated;
}
