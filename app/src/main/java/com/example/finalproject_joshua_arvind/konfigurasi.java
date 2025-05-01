package com.example.finalproject_joshua_arvind;

public class konfigurasi {
    // Change from phpmyadmin to the actual location of your PHP files
    // This should be the path to your PHP files on the web server
    // For testing on localhost with tools like XAMPP, use your computer's local IP address
    public static final String URL_BASE = "http://10.4.12.45/Android/phpmyadmin/register.php";

    public static final String URL_LOGIN = URL_BASE + "login.php";
    public static final String URL_REGISTER = URL_BASE + "register.php";
    public static final String URL_GET_USER_DETAILS = URL_BASE + "getUserDetails.php";
}