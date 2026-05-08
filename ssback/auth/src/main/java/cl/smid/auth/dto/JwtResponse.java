package cl.smid.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    
    private String token;
    
    @Builder.Default
    private String type = "Bearer"; 
    
    private String email;
    private String fullName;
    private List<String> roles;
}