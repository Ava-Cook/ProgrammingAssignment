document.addEventListener('DOMContentLoaded', function () {
    const enrollButtons = document.querySelectorAll('.enroll-btn');
    const creditDisplay = document.getElementById('currentCredits');
    let currentCredits = parseInt(creditDisplay.textContent);
    const maxCredits = 40;
    const selectedCourses = new Set();

    enrollButtons.forEach(button => {
        const courseID = button.dataset.courseid;
        const courseCredits = parseInt(button.dataset.credits);

        button.addEventListener('click', () => {
            if (button.classList.contains('disabled')) return;

            if (selectedCourses.has(courseID)) {
                // Deselect course
                selectedCourses.delete(courseID);
                currentCredits -= courseCredits;
                document.getElementById(`input-${courseID}`).value = "";
                button.textContent = "Enroll";
            } else {
                // Check if adding would go over the limit
                if (currentCredits + courseCredits > maxCredits) return;

                selectedCourses.add(courseID);
                currentCredits += courseCredits;
                document.getElementById(`input-${courseID}`).value = courseID;
                button.textContent = "Selected";
            }

            // Update credit display
            creditDisplay.textContent = currentCredits;

            // Disable other buttons if at limit
            enrollButtons.forEach(btn => {
                const btnCredits = parseInt(btn.dataset.credits);
                const btnCourse = btn.dataset.courseid;
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

    // Keep track of full courses to avoid re-enabling them
    const fullCourses = new Set();
    enrollButtons.forEach(button => {
        if (button.textContent === "Full") {
            fullCourses.add(button.dataset.courseid);
        }
    });
});
