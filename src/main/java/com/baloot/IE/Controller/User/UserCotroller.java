//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/Student")
//public class OfferingController {
//
//    @GetMapping("/{courseCode}/{classCode}")
//    public Offering getOffering(
//            @PathVariable String courseCode,
//            @PathVariable String classCode,
//            final HttpServletResponse response) throws IOException {
//        System.out.println("in get student");
//        try{
//            Offering offering = Bolbolestan.getInstance().getOffering(courseCode, classCode);
//            response.sendError(HttpStatus.OK.value());
//            return offering;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
//            return null;
//        }
//    }
//
//    @PostMapping("/search")
//    public ResponseEntity searchForCourses(
//            @RequestBody SearchData searchData) throws IOException {
//        try {
//            List<Offering> searchResult = SearchService.searchKeyword(searchData);
//            return ResponseEntity.status(HttpStatus.OK).body(searchResult);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("student not found. invalid login");
//        }
//    }
//
//    @DeleteMapping("")
//    public ResponseEntity removeCourse(
//            @RequestParam String courseCode,
//            @RequestParam String classCode) throws IOException {
//        System.out.println("in remove course");
//        Bolbolestan bolbolestan = Bolbolestan.getInstance();
//        if (bolbolestan.isAnybodyLoggedIn()) {
//            try {
//                String loggedInStudentId = bolbolestan.getLoggedInId();
//                bolbolestan.removeFromWeeklySchedule(loggedInStudentId, courseCode, classCode);
//                System.out.println("remove successful");
//                return ResponseEntity.status(HttpStatus.OK).body("OK");
//            } catch (Exception e) {
//                System.out.println("remove failed");
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//            }
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("student not found. invalid login");
//    }
//    @PutMapping("/wait")
//    public ResponseEntity waitForCourse(
//            @RequestParam String courseCode,
//            @RequestParam String classCode) throws IOException {
//    }
//
//    @PostMapping("/reset")
//    public ResponseEntity resetSelections() throws IOException {
//    }
//
//
//}
