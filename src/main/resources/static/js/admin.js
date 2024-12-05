console.log("Admin");

document.querySelector("#picture").addEventListener("change", function (event) {
  var file = event.target.files[0];
  var reader = new FileReader();
  reader.onload = function () {
    document.getElementById("imagePreview").src = reader.result;
  };
  reader.readAsDataURL(file);
});
