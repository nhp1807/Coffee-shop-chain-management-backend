function showCreateAccountForm() {
    document.getElementById("createAccountPanel").style.display = "block";
}

function hideCreateAccountForm() {
    document.getElementById("createAccountPanel").style.display = "none";
}

function showUpdateAccountForm() {
    document.getElementById("updateAccountPanel").style.display = "block";
}

function hideUpdateAccountForm() {
    document.getElementById("updateAccountPanel").style.display = "none";
}

function loadAccountDetails(accountId) {
    fetch(`/api/account/get/${accountId}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById("updateAccountID").value = data.accountID;
            document.getElementById("updateUsername").value = data.username;
            document.getElementById("updatePassword").value = data.password;
            document.getElementById("updateRole").value = data.role;
            document.getElementById("updateEmail").value = data.email;
            showUpdateAccountForm();
        })
        .catch(error => console.error('Error loading account details:', error));
}

function createAccount() {
    const account = {
        username: document.getElementById("createUsername").value,
        password: document.getElementById("createPassword").value,
        role: document.getElementById("createRole").value,
        email: document.getElementById("createEmail").value
    };

    fetch("/api/account/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(account)
    }).then(response => {
        if (response.ok) location.reload();
        else alert("Failed to create account");
    });
}

function updateAccount() {
    const accountId = document.getElementById("updateAccountID").value;
    const account = {
        username: document.getElementById("updateUsername").value,
        password: document.getElementById("updatePassword").value,
        role: document.getElementById("updateRole").value,
        email: document.getElementById("updateEmail").value
    };

    if (confirm("Are you sure you want to update this account?")) {
        fetch(`/api/account/update/${accountId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(account)
        }).then(response => {
            if (response.ok) location.reload();
            else alert("Failed to update account");
        });
    }
}

function deleteAccount(accountId) {
    if (confirm("Are you sure you want to delete this account?")) {
        fetch(`/api/account/delete/${accountId}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) location.reload();
                else alert("Failed to delete account");
            });
    }
}

function logout() {
    fetch('/logout', {
        method: 'POST',
        credentials: 'include' // Đảm bảo cookie phiên được gửi cùng yêu cầu
    })
        .then(response => {
            if (response.ok) {
                // Nếu logout thành công, chuyển hướng đến trang đăng nhập hoặc trang chủ
                window.location.href = '/login'; // Thay thế với đường dẫn của bạn
            } else {
                alert('Logout failed. Please try again.');
            }
        })
        .catch(error => {
            console.error('Error during logout:', error);
            alert('An error occurred during logout. Please try again.');
        });
}
