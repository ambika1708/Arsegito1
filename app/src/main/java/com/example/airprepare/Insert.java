package com.example.airprepare;


public class Insert {

    public String email;

    public String password;

    public String id;

    public String number;

    public Insert(String id, String a, String email, String password) {

    }

    public Insert(String id,String number,String name,String email,String password){



        this.id=id;

        this.number=number;

        this.email=email;

        this.password=password;

    }



    public String getEmail() {

        return email;

    }



    public String getPassword() {

        return password;

    }



    public String getId() {

        return id;

    }



    public String getNumber() {

        return number;

    }

}