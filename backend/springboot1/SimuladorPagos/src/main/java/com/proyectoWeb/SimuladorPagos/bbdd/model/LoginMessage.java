package com.proyectoWeb.SimuladorPagos.bbdd.model;

/**
 * Represents a login message containing user credentials.
 * <p>
 * This class encapsulates the information required for a login attempt,
 * including a user ID and password.
 */
public class LoginMessage {

    /**
     * The unique identifier for the user.
     */
    private Integer userId;

    /**
     * The name for the user
     */
    private String userName;

    /**
     * The password for the user.
     */
    private String password;

    /**
     * Default constructor.
     * <p>
     * Creates an empty {@link LoginMessage} object.
     */
    public LoginMessage() {
    }

    /**
     * Constructor with parameters.
     * <p>
     * Initializes the {@link LoginMessage} with the specified user ID and password.
     *
     * @param userId   The unique identifier for the user.
     * @param userName The name for the user
     * @param password The password for the user.
     */
    public LoginMessage(Integer userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Gets the user ID.
     *
     * @return The unique identifier for the user.
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId The new unique identifier for the user.
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Gets the userName.
     *
     * @return The name for the user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user ID.
     *
     * @param userName The new userName for the user.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password.
     *
     * @return The password for the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password The new password for the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

