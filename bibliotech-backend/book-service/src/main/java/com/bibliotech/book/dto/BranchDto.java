package com.bibliotech.book.dto;


public class BranchDto {
    private Long id;
    private String name;
    private String location;

    public BranchDto() {
    }

    public BranchDto(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BranchDto that = (BranchDto) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(name, that.name) &&
                java.util.Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, location);
    }

    @Override
    public String toString() {
        return "BranchDto{" +
                "id=" + id + ", " + "name=" + name + ", " + "location=" + location +
                '}';
    }
}
