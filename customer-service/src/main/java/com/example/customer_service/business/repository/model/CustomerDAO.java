package com.example.customer_service.business.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class CustomerDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @Column(name = "surname", length = 45, nullable = false)
    private String surname;

    @Column(name = "customer_identity_number")
    private String identityNumber;

    /*@OneToMany(mappedBy = "customerDAO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AccountDAO> accounts;

    /*@OneToMany(mappedBy = "loansDAO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LoanDAO> loans;

    @OneToMany(mappedBy = "creditCardDAO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CreditCardDAO> creditCards;*/

}

