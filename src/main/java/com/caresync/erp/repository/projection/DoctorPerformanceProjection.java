package com.caresync.erp.repository.projection;

public class DoctorPerformanceProjection {

    private final Long doctorId;
    private final String doctorName;
    private final String specialization;
    private final Long total;
    private final Long completed;
    private final Long cancelled;

    public DoctorPerformanceProjection(Long doctorId, String doctorName, String specialization,
                                       Long total, Long completed, Long cancelled) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.total = total;
        this.completed = completed;
        this.cancelled = cancelled;
    }

    public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getSpecialization() { return specialization; }
    public Long getTotal() { return total; }
    public Long getCompleted() { return completed; }
    public Long getCancelled() { return cancelled; }
}
