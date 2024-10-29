function showCreateAccountForm() {
    window.location.href = "/admin/account/create";
}

function deleteAccount(id) {
    if (confirm("Are you sure you want to delete this account?")) {
        fetch(`/admin/account/delete/${id}`, {
            method: 'POST',
        }).then(response => {
            if (response.ok) {
                alert("Account deleted successfully.");
                window.location.reload();
            } else {
                alert("Failed to delete account.");
            }
        });
    }
}

function loadAccountDetails(accountId) {
    fetch(`/api/account/get/${accountId}`)
        .then(response => response.json())
        .then(data => {
            // Giả sử bạn đã có các trường trong form cập nhật
            document.querySelector('input[name="username"]').value = data.username;
            document.querySelector('input[name="password"]').value = data.password; // Nếu cần
            document.querySelector('input[name="role"]').value = data.role;
            // Có thể thêm các trường khác nếu cần
            // Thay đổi URL của form để cập nhật tài khoản
            const updateForm = document.querySelector('form');
            updateForm.setAttribute('action', `/admin/account/update/${data.accountID}`);
        })
        .catch(error => console.error('Error loading account details:', error));
}
