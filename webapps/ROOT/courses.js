//implements business logic: Max course enrollment, Course is full, student already enrolled in a course
//won't let the user click the enroll button to enroll if any of these things are true
document.addEventListener('DOMContentLoaded', function () {
    const enrollButtons = document.querySelectorAll('.enroll-btn');
    const creditDisplay = document.getElementById('currentCredits');
    const hiddenInputs = document.querySelectorAll('input[name="selectedCourses"]');
    let currentCredits = parseInt(creditDisplay.textContent);
    const maxCredits = 40;
    const selectedCourses = new Set();
    const enrolledCourses = new Set();
    const fullCourses = new Set();

    //changes the text of the next of the button if enrolled or full
    enrollButtons.forEach(button => {
        const courseID = button.dataset.courseid;
        const courseCredits = parseInt(button.dataset.credits);

        if (button.textContent.trim().toLowerCase() === "enrolled") {
            enrolledCourses.add(courseID);
        }

        if (button.textContent.trim().toLowerCase() === "full") {
            fullCourses.add(button.dataset.courseid);
        }
    });

    //allows the user to click the buttons to enroll and checks business logic    
    enrollButtons.forEach(button => {
        const courseID = button.dataset.courseid;
        const courseCredits = parseInt(button.dataset.credits);
        const inputCheckbox = document.getElementById(`input-${courseID}`);

        button.addEventListener('click', () => {
            if (button.classList.contains('disabled') || enrolledCourses.has(courseID)) return;

            if (selectedCourses.has(courseID)) {
                // Deselect course
                selectedCourses.delete(courseID);
                currentCredits -= courseCredits;
                document.getElementById(`input-${courseID}`).value = "";
                button.textContent = "Enroll";
                if (inputCheckbox) {
                    inputCheckbox.checked = false;
                }
            } else {
                // Check if adding would go over the credit limit
                if (currentCredits + courseCredits > maxCredits) return;

                selectedCourses.add(courseID);
                currentCredits += courseCredits;
                document.getElementById(`input-${courseID}`).value = courseID;
                button.textContent = "Selected";

                if (inputCheckbox) {
                    inputCheckbox.checked = true;
                }
            }

            // Update credit display
            creditDisplay.textContent = currentCredits;

            // Disable other buttons if at limit/full/enrolled
            enrollButtons.forEach(btn => {
                const btnCredits = parseInt(btn.dataset.credits);
                const btnCourse = btn.dataset.courseid;
                const btnInput = document.getElementById(`input-${btnCourse}`);
                if (!selectedCourses.has(btnCourse) && (currentCredits + btnCredits > maxCredits)) {
                    btn.classList.add('disabled');
                    btn.disabled = true;
                    btn.title = "Credit limit reached";
                } else if (enrolledCourses.has(btnCourse)) {
                    btn.classList.add('disabled');
                    btn.disabled = true;
                    btn.title = "Already enrolled";
                }else if (fullCourses.has(btnCourse)) {
                    btn.classList.add('disabled');
                    btn.disabled = true;
                    btn.title = "Course is full";
                }  else {
                    btn.classList.remove('disabled');
                    btn.disabled = false;
                    btn.title = "";
                
                }
            });
        });

        button.addEventListener('click', function() {
            if (!button.classList.contains('disabled')) {
                button.classList.toggle('selected');
            }
        });
    });
});