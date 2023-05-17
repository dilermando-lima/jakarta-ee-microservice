package demo.datasource;

import java.util.Objects;
import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class EntityManagerCommit  {

    public class EntityManagerCommitException extends RuntimeException {
        public EntityManagerCommitException(String msg,Throwable throwable){
            super(msg,throwable);
        }
    }

    private final EntityManager entityManager;
    private final EntityTransaction transaction;
    private boolean open = false;

    public EntityManagerCommit(EntityManager entityManager) {
        Objects.requireNonNull(entityManager);
        this.entityManager = entityManager;
        this.transaction = this.entityManager.getTransaction();
    }

    public static EntityManagerCommit init(EntityManager entityManager){
        return new EntityManagerCommit(entityManager);
    }

    public EntityManagerCommit exec(Consumer<EntityManager> consumer){
        try {
            open();
            consumer.accept(entityManager);
        }catch(Exception e){
            rollback();
            throw new EntityManagerCommitException(e.getMessage(),e);
        }
        return this;
    }

    private void open(){
        if( !open ){
            entityManager.getTransaction().begin();
            open = true;
        }
    }

    private void rollback(){
        if (entityManager.getTransaction() != null && entityManager.getTransaction().isActive()) {
            transaction.rollback();
            open = false;
        }
    }

    public void commit(){
        try {
            open();
            entityManager.getTransaction().commit();
            open = false;
        }catch(Exception e){
            rollback();
            throw new EntityManagerCommitException(e.getMessage(),e);
        }
    }


    
    
}
