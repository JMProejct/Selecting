document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    if (params.get('error') === 'unauthorized') {
        const modal = document.getElementById("errorModal");
        if (modal) {
            modal.classList.remove("hidden");
            setTimeout(() => modal.classList.add("hidden"), 3000);
        }
    }
});