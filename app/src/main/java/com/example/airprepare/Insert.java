package com.example.airprepare;



import android.widget.EditText;



public class Insert {

    String name;

    String email;

    String password;

    String id;

    String number;

    public  Insert(){

    }

    public Insert(String id,String number,String name,String email,String password){



        this.id=id;

        this.number=number;

        this.name=name;

        this.email=email;

        this.password=password;

    }



    public String getName() {

        return name;

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