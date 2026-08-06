// ==================================================
// ADMIN.JS - Fonctions avancées pour l'administration
// ==================================================

// Gestion du thème (dark/light)
document.addEventListener('DOMContentLoaded', function() {
    // Theme toggle
    const themeBtn = document.getElementById('themeToggle');
    if (themeBtn) {
        themeBtn.addEventListener('click', function() {
            const current = document.documentElement.getAttribute('data-theme');
            const newTheme = current === 'dark' ? 'light' : 'dark';
            document.documentElement.setAttribute('data-theme', newTheme);
            localStorage.setItem('theme', newTheme);
        });
    }

    // Restaurer le thème
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        document.documentElement.setAttribute('data-theme', savedTheme);
    }

    // Filtres dynamiques sur les tableaux
    const filterSelects = document.querySelectorAll('.filter-select');
    filterSelects.forEach(select => {
        select.addEventListener('change', function() {
            const tableId = this.dataset.table;
            const filterKey = this.dataset.filter;
            const filterValue = this.value;
            const rows = document.querySelectorAll(`#${tableId} tbody tr`);
            rows.forEach(row => {
                const cell = row.querySelector(`td[data-${filterKey}]`);
                if (cell) {
                    const cellValue = cell.getAttribute(`data-${filterKey}`);
                    if (filterValue === '' || cellValue === filterValue) {
                        row.style.display = '';
                    } else {
                        row.style.display = 'none';
                    }
                }
            });
        });
    });

    // Recherche en temps réel
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keyup', function() {
            const query = this.value.toLowerCase();
            const rows = document.querySelectorAll('#testsTable tbody tr');
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(query) ? '' : 'none';
            });
        });
    }

    // Export CSV
    const exportBtn = document.getElementById('exportCsv');
    if (exportBtn) {
        exportBtn.addEventListener('click', function() {
            const url = '/admin/export/tests?format=csv';
            window.location.href = url;
        });
    }
    const exportPdfBtn = document.getElementById('exportPdf');
    if (exportPdfBtn) {
        exportPdfBtn.addEventListener('click', function() {
            const url = '/admin/export/tests?format=pdf';
            window.location.href = url;
        });
    }

    // Animation 3D sur les cartes
    document.querySelectorAll('.card-3d').forEach(card => {
        card.addEventListener('mousemove', function(e) {
            const rect = this.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            const centerX = rect.width / 2;
            const centerY = rect.height / 2;
            const rotateX = (y - centerY) / 20;
            const rotateY = (centerX - x) / 20;
            this.querySelector('.card').style.transform =
                `rotateX(${rotateX}deg) rotateY(${rotateY}deg) scale(1.02)`;
        });
        card.addEventListener('mouseleave', function() {
            this.querySelector('.card').style.transform = 'rotateX(0) rotateY(0) scale(1)';
        });
    });
});

// ==================================================
// FONCTIONS UTILES
// ==================================================
function confirmDelete(message) {
    return confirm(message || 'Are you sure?');
}

// Exemple d'utilisation pour les boutons de suppression
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('delete-btn')) {
        if (!confirmDelete('Delete this item?')) {
            e.preventDefault();
        }
    }
});