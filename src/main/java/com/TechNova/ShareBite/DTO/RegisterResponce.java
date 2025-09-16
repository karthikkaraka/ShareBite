package com.TechNova.ShareBite.DTO;

import com.TechNova.ShareBite.Model.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponce {
    private long userid;
    private String username;
    private Roles role;
}
