package com.projet_restaurant.serviceutilisateurs.web;
        import com.projet_restaurant.serviceutilisateurs.Dto.UserDTO;
        import com.projet_restaurant.serviceutilisateurs.Service.BaseService;
        import io.swagger.v3.oas.annotations.OpenAPIDefinition;
        import io.swagger.v3.oas.annotations.Operation;
        import io.swagger.v3.oas.annotations.Parameter;
        import io.swagger.v3.oas.annotations.info.Info;
        import io.swagger.v3.oas.annotations.responses.ApiResponse;
        import io.swagger.v3.oas.annotations.servers.Server;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
       // import org.springframework.security.access.prepost.PreAuthorize;
        import org.springframework.security.crypto.password.PasswordEncoder;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:4200")
@OpenAPIDefinition(
        info = @Info(
                title = "Service Utilisateur",
                description = " Gerer des utilisateurs",
                version = "3.0.1"
        ),

        servers = @Server(
                url = "http://localhost:8082/"
        )
)
public class UserWeb  {

    private final BaseService<UserDTO, Long> baseService;

    @Autowired
    public UserWeb(BaseService<UserDTO, Long> baseService , PasswordEncoder passwordEncoder) {
        this.baseService = baseService;
    }
    @PostMapping()
    @Operation(summary = "Créer un nouvel utilisateur",
            description = "Cette route permet de créer un nouvel utilisateur.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données invalides")
            })
    public ResponseEntity<UserDTO> createUser(@RequestBody @Parameter(description = "Utilisateur à créer") UserDTO userDTO) {
        UserDTO createdUser = baseService.create(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    /*@PreAuthorize("hasRole('ADMIN')")*/
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un utilisateur",
            description = "Cette route permet de mettre à jour les informations d'un utilisateur existant.")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = baseService.update(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un utilisateur par ID",
            description = "Cette route permet d'obtenir les détails d'un utilisateur en utilisant son ID.")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = baseService.getById(id);
        return ResponseEntity.ok(userDTO);
    }
    /*@PreAuthorize("hasRole('ADMIN')")*/
    @GetMapping
    @Operation(summary = "Obtenir tous les utilisateurs",
            description = "Cette route permet d'obtenir une liste de tous les utilisateurs.")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = baseService.getAll();
        return ResponseEntity.ok(userDTOs);
    }
    /*@PreAuthorize("hasRole('ADMIN')")*/
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur",
            description = "Cette route permet de supprimer un utilisateur en utilisant son ID.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        baseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
