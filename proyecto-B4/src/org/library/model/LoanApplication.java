package org.library.model;

import java.time.LocalDate;
import org.library.enums.RequestStatus;

public class LoanApplication {
    private Integer request_id;
    private Integer student_id;
    private LocalDate request_date;
    private RequestStatus status;
    private String observation;
    private Integer librarian_id;
    private LocalDate response_date;

    public LoanApplication() {}

    public LoanApplication(Integer request_id, Integer student_id, LocalDate request_date, RequestStatus status, String observation, Integer librarian_id, LocalDate response_date) {
        this.request_id = request_id;
        this.student_id = student_id;
        this.request_date = request_date;
        this.status = status;
        this.observation = observation;
        this.librarian_id = librarian_id;
        this.response_date = response_date;
    }

    public Integer getRequest_id() { return request_id; }
    public void setRequest_id(Integer request_id) { this.request_id = request_id; }
    public Integer getStudent_id() { return student_id; }
    public void setStudent_id(Integer student_id) { this.student_id = student_id; }
    public LocalDate getRequest_date() { return request_date; }
    public void setRequest_date(LocalDate request_date) { this.request_date = request_date; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
    public Integer getLibrarian_id() { return librarian_id; }
    public void setLibrarian_id(Integer librarian_id) { this.librarian_id = librarian_id; }
    public LocalDate getResponse_date() { return response_date; }
    public void setResponse_date(LocalDate response_date) { this.response_date = response_date; }
}
