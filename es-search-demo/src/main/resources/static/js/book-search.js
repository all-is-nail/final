// Function to handle book search
function searchBooks() {
    const searchTerm = document.getElementById('searchInput').value.trim();
    if (!searchTerm) {
        // Clear results if search term is empty
        document.getElementById('results').innerHTML = '';
        return;
    }
    
    // Show loading state
    document.getElementById('results').innerHTML = '<p>Searching...</p>';
    
    // Make API request
    fetch(`/api/book/search?name=${encodeURIComponent(searchTerm)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(books => {
            displayResults(books);
        })
        .catch(error => {
            console.error('Error searching for books:', error);
            document.getElementById('results').innerHTML = 
                '<p class="no-results">Error searching for books. Please try again later.</p>';
        });
}

// Function to handle live search
function liveSearch() {
    const searchTerm = document.getElementById('liveSearchInput').value.trim();
    if (!searchTerm || searchTerm.length <= 2) {
        // Clear results if search term is empty or too short
        document.getElementById('results').innerHTML = '';
        return;
    }
    
    // Show loading state
    document.getElementById('results').innerHTML = '<p>Searching...</p>';
    
    // Make API request
    fetch(`/api/book/search?name=${encodeURIComponent(searchTerm)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(books => {
            displayResults(books);
        })
        .catch(error => {
            console.error('Error searching for books:', error);
            document.getElementById('results').innerHTML = 
                '<p class="no-results">Error searching for books. Please try again later.</p>';
        });
}

// Function to display search results
function displayResults(books) {
    const resultsDiv = document.getElementById('results');
    
    if (!books || books.length === 0) {
        resultsDiv.innerHTML = '<p class="no-results">No books found matching your search.</p>';
        return;
    }
    
    let html = `<h2>Found ${books.length} book(s):</h2>`;
    
    books.forEach(book => {
        html += `
            <div class="book-item">
                <div class="book-name">${book.name}</div>
                <div class="book-details">
                    <p>Author: ${book.author || 'N/A'}</p>
                    <p>Category: ${book.category || 'N/A'}</p>
                    <p>Publisher: ${book.publisher || 'N/A'}</p>
                    <p>Price: $${book.price || '0'}</p>
                    <p>Description: ${book.description || 'No description available'}</p>
                </div>
            </div>
        `;
    });
    
    resultsDiv.innerHTML = html;
}

// Debounce function to limit how often the search is triggered
function debounce(func, delay) {
    let timeoutId;
    return function(...args) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => {
            func.apply(this, args);
        }, delay);
    };
}

// Initialize event listeners when the DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    // Add event listener for Enter key press on regular search
    document.getElementById('searchInput').addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            searchBooks();
        }
    });
    
    // Add event listener for live search with debounce
    const debouncedLiveSearch = debounce(liveSearch, 300);
    document.getElementById('liveSearchInput').addEventListener('input', debouncedLiveSearch);
}); 