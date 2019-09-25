package net.anet.workflow.airflow.service.dto;

public class AfDbColTypeDTO {
    private long id;
    private String name;
    private AfAnonTypeDTO anonType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AfAnonTypeDTO getAnonType() {
        return anonType;
    }

    public void setAnonType(AfAnonTypeDTO anonType) {
        this.anonType = anonType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AfDbColTypeDTO that = (AfDbColTypeDTO) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "AfDbColTypeDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", anonType=" + anonType +
            '}';
    }
}
