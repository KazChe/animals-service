console.log('main.js loaded');
document.addEventListener('DOMContentLoaded', () => {
    const saveButton = document.getElementById('saveButton');
    const showLatestButton = document.getElementById('showLatestButton');
    const imageContainer = document.getElementById('imageContainer');
    const status = document.getElementById('status');

    saveButton.addEventListener('click', async () => {
        try {
            const type = document.getElementById('animalType').value;
            const count = parseInt(document.getElementById('imageCount').value);
            
            imageContainer.innerHTML = '';
            status.textContent = 'Loading images...';
            
            // Get image URLs from server
            const response = await fetch(`/api/images/save/${type}?count=${count}`, {
                method: 'POST'
            });
            
            if (!response.ok) {
                throw new Error('Failed to save images');
            }
            
            const imageUrls = await response.json();
            
            // Display each image
            imageUrls.forEach(url => {
                const img = document.createElement('img');
                img.src = url;
                img.style.maxWidth = '400px';
                img.style.margin = '10px';
                imageContainer.appendChild(img);
            });
            
            status.textContent = `${count} images saved successfully!`;
        } catch (error) {
            console.error('Error:', error);
            status.textContent = 'Error: ' + error.message;
        }
    });

    showLatestButton.addEventListener('click', async () => {
        try {
            const type = document.getElementById('animalType').value;
            imageContainer.innerHTML = '';
            
            const response = await fetch(`/api/images/latest/${type}`);
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Failed to fetch latest image. You might need to save an image first.');
            }
            
            const imageUrl = await response.text();
            const img = document.createElement('img');
            img.src = imageUrl;
            img.style.maxWidth = '400px';
            img.style.margin = '10px';
            imageContainer.appendChild(img);
        } catch (error) {
            console.error('Error:', error);
            status.textContent = error.message;
        }
    });
}); 