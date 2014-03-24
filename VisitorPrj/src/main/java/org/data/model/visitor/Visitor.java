package org.data.model.visitor;

import org.data.model.Contractor;
import org.data.model.PermanentEmployee;

public interface Visitor {
    void visit(PermanentEmployee employee);

    void visit(Contractor employee);
}
