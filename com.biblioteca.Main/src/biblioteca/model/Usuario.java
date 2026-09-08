package biblioteca.model;

import biblioteca.enums.Rol;

public class Usuario {
    private Integer user_id;
    private String user_code;
    private String first_name;
    private String last_name;
    private String email;
    private String password_hash;
    private Rol user_role;
    private Boolean active;

    public Usuario() {}

    public Usuario(Integer user_id, String user_code, String first_name, String last_name, String email, String password_hash, Rol user_role, Boolean active) {
        this.user_id = user_id;
        this.user_code = user_code;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password_hash = password_hash;
        this.user_role = user_role;
        this.active = active;
    }

    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }
    public String getUser_code() { return user_code; }
    public void setUser_code(String user_code) { this.user_code = user_code; }
    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword_hash() { return password_hash; }
    public void setPassword_hash(String password_hash) { this.password_hash = password_hash; }
    public Rol getUser_role() { return user_role; }
    public void setUser_role(Rol user_role) { this.user_role = user_role; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
