console.log("modal");

const viewContactModel = document.getElementById("default-modal");
const options = {
  placement: "bottom-right",
  backdrop: "dynamic",
  backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
  closable: true,
  onHide: () => {
    console.log("modal is hidden");
  },
  onShow: () => {
    console.log("modal is shown");
  },
  onToggle: () => {
    console.log("modal has been toggled");
  },
};

// instance options object
const instanceOptions = {
  id: "default-modal",
  override: true,
};
const contactModel = new Modal(viewContactModel, options, instanceOptions);

function openModel() {
  contactModel.show();
}

function closeModel() {
  contactModel.hide();
}

function loadContactData(id) {
  console.log(id);
  fetch("http://localhost:8081/api/contact/" + id)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      document.querySelector("#contact_name").textContent = data.name;
      document.querySelector("#contact_image").src = data.picture;
      document.querySelector("#contact_email").textContent = data.email;
      document.querySelector("#contact_phone").textContent = data.phoneNumber;
      document.querySelector("#contact_address").textContent = data.address;
      document.querySelector("#contact_about").textContent = data.description;
      if (data.favorite == true) {
        document.querySelector("#contact_favorite").textContent =
          "This is your favorite contact";
      } else {
        document.querySelector("#contact_favorite").textContent =
          "This is not your favorite contact";
      }
      openModel();
    });
}

function deleteContact(id) {
  Swal.fire({
    title: "Do you want to delete the contact?",
    icon: "warning",
    showDenyButton: true,
    confirmButtonText: "Yes",
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      const url = "http://localhost:8081/user/contact/delete/" + id;
      window.location.replace(url);
      Swal.fire("Deleted!", "", "success");
    } else if (result.isDenied) {
      Swal.fire("Contact is not deleted", "", "info");
    }
  });
}

function exportData() {
  TableToExcel.convert(document.getElementById("tableContent"), {
    name: "contact.xlsx",
    sheet: {
      name: "Sheet 1",
    },
  });
}
