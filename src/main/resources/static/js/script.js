console.log("first");

let currentTheme = getThemeFromLocalStorage();
let changeThemeButton = document.querySelector("#theme_change_button");
let themeText = document.querySelector("#theme_change_button span");

document.addEventListener("DOMContentLoaded", function () {
  changeTheme();
});
changeThemeButton.addEventListener("click", function () {
  if (localStorage.getItem("theme") == "light") {
    document.querySelector("html").classList.replace("light", "dark");
    themeText.innerHTML = "Light";
    localStorage.setItem("theme", "dark");
  } else {
    document.querySelector("html").classList.replace("dark", "light");
    themeText.innerHTML = "Dark";
    localStorage.setItem("theme", "light");
  }
});

function changeTheme() {
  document.querySelector("html").classList.add(currentTheme);
  if (currentTheme == "dark") themeText.innerHTML = "Light";
  else themeText.innerHTML = "Dark";
}

function setThemeToLocalStorage(theme) {
  localStorage.setItem("theme", theme);
}

function getThemeFromLocalStorage() {
  return localStorage.getItem("theme") == null
    ? "light"
    : localStorage.getItem("theme");
}
