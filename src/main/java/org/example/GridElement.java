package org.example;

class GridElement {
    private ElementType type;
    private boolean called;
    private Owner owner;

    public GridElement() {
        type = ElementType.EMPTY;
        called = false;
        owner = Owner.NONE;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public boolean isCalled() {
        return called;
    }

    public void setCalled(boolean called) {
        this.called = called;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}