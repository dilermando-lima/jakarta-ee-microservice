package test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
    @Table(name = "company")
    public class Company{       
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @Column(name = "id")
        String id;

        @Column(name = "name")
        String name;

        public Company(){}

        public Company(String name){
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }