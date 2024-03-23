package com.testehan.protobuf.ex6;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import com.testehan.models.ex06.Credentials;
import com.testehan.models.ex06.Email;
import com.testehan.models.ex06.Phone;

public class OneOfDemo {

    public static void main(String[] args) {
        var email = Email.newBuilder().setEmail("df@yhspp.com").build();
        var phone = Phone.newBuilder().setPhone("1234566").build();

        var credentials = Credentials.newBuilder()
                .setContactEmail(email)
                .setContactPhone(phone)
//                .setContactEmail(email)
                .build();

        System.out.println(credentials.getContactEmail());  // prints email as long as the phone is not set
        System.out.println(credentials.getContactPhone());  // if phone is set, then it will erase previously set email

        switch (credentials.getContactCase()){
            case CONTACTEMAIL -> System.out.println(credentials.getContactEmail());
            case CONTACTPHONE -> System.out.println(credentials.getContactPhone());
        }
    }
}
