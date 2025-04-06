document.addEventListener('DOMContentLoaded', function () {
    const enrollButtons = document.querySelectorAll('.enroll-btn');
    const creditDisplay = document.getElementById('currentCredits');
    const hiddenInputs = document.querySelectorAll('input[name="selectedCourses"]');
    let currentCredits = parseInt(creditDisplay.textContent);
    const maxCredits = 40;
    const selectedCourses = new Set();

    const fullCourses = new Set();
    enrollButtons.forEach(button => {
        if (button.textContent.trim().toLowerCase() === "full") {
            fullCourses.add(button.dataset.courseid);
        }
    });

    enrollButtons.forEach(button => {
        const courseID = button.dataset.courseid;
        const courseCredits = parseInt(button.dataset.credits);
        const inputCheckbox = document.getElementById(`input-${courseID}`);

        button.addEventListener('click', () => {
            if (button.classList.contains('disabled')) return;

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
                // Check if adding would go over the limit
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

            // Disable other buttons if at limit
            enrollButtons.forEach(btn => {
                const btnCredits = parseInt(btn.dataset.credits);
                const btnCourse = btn.dataset.courseid;
                const btnInput = document.getElementById(`input-${btnCourse}`);
                if (!selectedCourses.has(btnCourse) && (currentCredits + btnCredits > maxCredits)) {
                    btn.classList.add('disabled');
                    btn.disabled = true;
                    btn.title = "Credit limit reached";
                } else if (!fullCourses.has(btnCourse)) {
                    btn.classList.remove('disabled');
                    btn.disabled = false;
                    btn.title = "";
                }
            });
        });
    });


});