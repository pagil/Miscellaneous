package org.data.model.visitor;

public interface Visitable {
    void accept(Visitor visitor);
}
