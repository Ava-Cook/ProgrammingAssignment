package seng2050;

public class StudentService {
    private StudentDAO studentDAO = new StudentDAOImpl();
   
    /*
     * Business logic to authenticate a student
     * - Returns null if the student is not authenticated,
     * - Otherwise return the student object
     * */
    public Student authenticateStudent(String stdNo, String password)
    {
        Student s  = studentDAO.getStudentByStdNo(stdNo); // Finds the student based on stdNo
        
        if (s!=null)
        {
            if (password.equals(s.getPasswordHash())) // Checks if passwords match
            {
                return s;
            }
        }
        return null;
    }
}
