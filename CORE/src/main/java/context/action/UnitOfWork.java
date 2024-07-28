package context.action;

import jakarta.transaction.SystemException;

import java.util.Objects;

public interface UnitOfWork<T> {

    void handle(T var1) throws SystemException;

    default UnitOfWork<T> andThen(UnitOfWork<? super T> after){
        Objects.requireNonNull(after);
        return (t)->{
            this.handle(t);
            after.handle(t);
        };
    }
}
