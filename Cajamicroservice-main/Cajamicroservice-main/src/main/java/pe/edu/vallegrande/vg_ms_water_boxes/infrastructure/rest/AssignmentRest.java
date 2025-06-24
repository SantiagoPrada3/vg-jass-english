package pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vg_ms_water_boxes.domain.models.Assignment;
import pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.repository.AssignmentRepository;
import pe.edu.vallegrande.vg_ms_water_boxes.domain.models.Box;
import pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.repository.BoxRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import org.bson.types.ObjectId;
import lombok.Data;

import static java.util.Locale.filter;

@RestController
@RequestMapping("/api/v1/assignments")
@CrossOrigin(origins = "*")
public class AssignmentRest {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private BoxRepository boxRepository;


    @GetMapping("/all")
    public Flux<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @GetMapping
    public Flux<Assignment> getAssignments(@RequestParam(required = false) String status) {
        return assignmentRepository.findAll()
                .filter(assignment -> status == null || assignment.getStatus().equalsIgnoreCase(status))
                .doOnNext(assignment -> System.out.println("Filtered assignment: " + assignment));
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Assignment> createAssignment(@RequestBody AssignmentRequest request) {
        // Create new assignment
        Assignment assignment = new Assignment();
        assignment.setBoxId(request.getBoxId());

        if (request.getUserData() != null) {
            Assignment.UserData userData = Assignment.UserData.builder()
                    .fullName(request.getUserData().getFullName())
                    .document(request.getUserData().getDocument())
                    .address(request.getUserData().getAddress())
                    .build();
            assignment.setUserData(userData);
        }

        // Set dates and status
        Date now = new Date();
        assignment.setRegistrationDate(now);
        assignment.setAssignmentDate(now);
        assignment.setStatus("active");

        // Generate a unique ID for the user if not provided
        String userId = new ObjectId().toString();
        assignment.setUserId(userId);

        // Find the box and process data reactively
        return boxRepository.findById(assignment.getBoxId())
                .switchIfEmpty(Mono.error(new RuntimeException("Box not found with id: " + assignment.getBoxId())))
                .flatMap(box -> {
                    // Automatically update box data in the assignment
                    Assignment.BoxData boxData = new Assignment.BoxData();
                    boxData.setCode(box.getCode());
                    boxData.setType(box.getType());
                    assignment.setBoxData(boxData);

                    // Save the assignment
                    return assignmentRepository.save(assignment)
                            .flatMap(savedAssignment -> {
                                // Update the box with the user ID
                                box.setUserId(userId);
                                return boxRepository.save(box)
                                        .thenReturn(savedAssignment);
                            });
                });
    }

    @GetMapping("/{id}")
    public Mono<Assignment> getAssignmentById(@PathVariable String id) {
        return assignmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Assignment not found with id: " + id)));
    }

    @PutMapping("/{id}")
    public Mono<Assignment> updateAssignment(@PathVariable String id, @RequestBody Assignment assignment) {
        return assignmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Assignment not found with id: " + id)))
                .flatMap(existingAssignment -> {
                    assignment.setId(id);
                    // Preserve immutable fields
                    assignment.setRegistrationDate(existingAssignment.getRegistrationDate());
                    assignment.setAssignmentDate(existingAssignment.getAssignmentDate());
                    assignment.setUserId(existingAssignment.getUserId());
                    assignment.setBoxId(existingAssignment.getBoxId());
                    return assignmentRepository.save(assignment);
                });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAssignment(@PathVariable String id) {
        return assignmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Assignment not found with id: " + id)))
                .flatMap(existingAssignment -> {
                    existingAssignment.setStatus("inactive");
                    return assignmentRepository.save(existingAssignment);
                })
                .then();
    }

    @GetMapping("/box/{boxId}")
    public Flux<Assignment> getAssignmentsByBoxId(@PathVariable String boxId) {
        return assignmentRepository.findByBoxId(boxId);
    }

    @GetMapping("/user/{userId}")
    public Flux<Assignment> getAssignmentsByUserId(@PathVariable String userId) {
        return assignmentRepository.findByUserId(userId);
    }

    @PatchMapping("/{id}/status")
    public Mono<Assignment> updateAssignmentStatus(@PathVariable String id,
                                                   @RequestBody AssignmentStatusUpdate statusUpdate) {
        return assignmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Assignment not found with id: " + id)))
                .flatMap(existingAssignment -> {
                    existingAssignment.setStatus(statusUpdate.getStatus() ? "active" : "inactive");
                    return assignmentRepository.save(existingAssignment);
                });
    }

    @PatchMapping("/{id}/restore")
    public Mono<Assignment> restoreAssignment(@PathVariable String id) {
        return assignmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Assignment not found with id: " + id)))
                .flatMap(existingAssignment -> {
                    existingAssignment.setStatus("active");
                    return assignmentRepository.save(existingAssignment);
                });
    }


    public static class AssignmentStatusUpdate {
        private Boolean status;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }
    }

    // DTO for assignment request
    @Data
    public static class AssignmentRequest {
        private String boxId;
        private UserData userData;

        // Getters for English fields
        public String getBoxId() {
            return boxId;
        }

        public UserData getUserData() {
            return userData;
        }

        // Optional getters that map Spanish field names to English ones for backward
        // compatibility
        public String getCajaId() {
            return boxId;
        }

        public UserData getUsuarioDatos() {
            return userData;
        }
    }

    @Data
    public static class UserData {
        private String fullName;
        private String document;
        private String address;

        // Convert to Assignment.UserData
        public Assignment.UserData toAssignmentUserData() {
            return Assignment.UserData.builder()
                    .fullName(fullName)
                    .document(document)
                    .address(address)
                    .build();
        }
    }
}