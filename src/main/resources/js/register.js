// document.getElementById('registerForm').addEventListener('submit', function(event) {
//     event.preventDefault();
//
//     const username = document.getElementById('inputUsername').value;
//     const fullName = document.getElementById('inputFullName').value;
//     const email = document.getElementById('inputEmail').value;
//     const age = document.getElementById('inputAge').value;
//     const level = document.getElementById('selectLevel').value;
//     const password = document.getElementById('inputPassword').value;
//     const confirmPassword = document.getElementById('inputConfirmPassword').value;
//
//
//     if (password !== confirmPassword) {
//         alert("Passwords do not match!");
//         return;
//     }
//
//
//     const userData = {
//         username: username,
//         fullName: fullName,
//         email: email,
//         age: age,
//         level: level,
//         password: password
//     };
//
//     console.log("User Data", userData);
//
//
//     alert("Registration successful! You can now login.");
//     window.location.href = "/login"; // Redirect to login page
// });
