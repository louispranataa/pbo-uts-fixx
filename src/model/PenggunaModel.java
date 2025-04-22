/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class PenggunaModel {
    private String id;
    private String username;
    private String passwordHash;
    private String name;

    public PenggunaModel(String id, String username, String passwordHash, String name) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.name = name;
    }

    public PenggunaModel(String id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    // Getter dan Setter
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getName() { return name; }

    public void setId(String id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setName(String name) { this.name = name; }
}
