package nl.fontys.s3.grp1.business.converter;

import nl.fontys.s3.grp1.domain.Account;
import nl.fontys.s3.grp1.domain.Journalist;
import nl.fontys.s3.grp1.persistence.entity.JournalistEntity;

public class JournalistConverter {
    private JournalistConverter(){}

    public static Journalist convert(JournalistEntity entity){
        Account account =
                Account.builder()

                        .email(entity.getAccount().getEmail())
                        .password(entity.getAccount().getPassword())
                        .build();


        return Journalist.builder()
                .id(entity.getId())
                .account(account)
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .birthday(entity.getBirthday())
                .build();
    }
}
