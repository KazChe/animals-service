// TODO: refactor this later - too messy
console.log("main.js loaded")

document.addEventListener("DOMContentLoaded", () => {
  const saveBtn = document.getElementById("saveButton")
  const showLatestBtn = document.getElementById("showLatestButton")
  const imgContainer = document.getElementById("imageContainer")
  const statusEl = document.getElementById("status")

    // FIXME: need better error handling here
    saveBtn.addEventListener("click", async () => {
    try {
      const type = document.getElementById("animalType").value
      const count = parseInt(document.getElementById("imageCount").value)

      // clear previous results
      imgContainer.innerHTML = ""
      statusEl.textContent = "Loading images..."

      console.log(`Fetching ${count} ${type} images...`) // debug

      const response = await fetch(`/api/images/save/${type}?count=${count}`, {
        method: "POST",
      })

      // handle errors
      if (!response.ok) {
        throw new Error("Failed to save images")
      }

      const imageUrls = await response.json()

      // dplay each image
      imageUrls.forEach((url) => {
        const img = document.createElement("img");
        img.src = url;
        img.style.maxWidth = "400px";
        img.style.margin = "10px";
        imgContainer.appendChild(img);
      });

      statusEl.textContent = `${count} images saved successfully!`;
    } catch (error) {
      console.error("Error:", error);
      statusEl.textContent = "Error: " + error.message;
    }
  });

  showLatestBtn.addEventListener("click", async () => {
    try {
      const type = document.getElementById("animalType").value;
      imgContainer.innerHTML = "";

      const response = await fetch(`/api/images/latest/${type}`);
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(
          errorText ||
            "Failed to fetch latest image. You might need to save some images first."
        );
      }

      const imageUrl = await response.text();
      const img = document.createElement("img");
      img.src = imageUrl;
      img.style.maxWidth = "400px";
      img.style.margin = "10px";
      imgContainer.appendChild(img);
    } catch (error) {
      console.error("Error:", error);
      statusEl.textContent = error.message;
    }
  });
});
