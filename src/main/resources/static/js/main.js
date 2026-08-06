// Toggle thème
document.getElementById('themeToggle')?.addEventListener('click', function() {
    const current = document.documentElement.getAttribute('data-theme');
    document.documentElement.setAttribute('data-theme', current === 'dark' ? 'light' : 'dark');
    this.innerHTML = current === 'dark' ? '<i class="fas fa-moon"></i>' : '<i class="fas fa-sun"></i>';
});

// Chatbot
const chatbot = document.getElementById('chatbot');
const chatbotButton = document.getElementById('chatbotButton');
const toggleBtn = document.getElementById('chatbotToggle');
let chatOpen = false;
function toggleChat() {
    chatOpen = !chatOpen;
    chatbot.style.display = chatOpen ? 'block' : 'none';
    chatbotButton.style.display = chatOpen ? 'none' : 'block';
}
chatbotButton.addEventListener('click', toggleChat);
toggleBtn.addEventListener('click', toggleChat);
document.getElementById('chatSend').addEventListener('click', function() {
    const input = document.getElementById('chatInput');
    const msg = input.value.trim();
    if (!msg) return;
    const messages = document.getElementById('chatMessages');
    messages.innerHTML += `<div class="text-end"><span class="badge bg-primary">You: ${msg}</span></div>`;
    messages.innerHTML += `<div class="text-start"><span class="badge bg-secondary">Bot: I'm here to help!</span></div>`;
    input.value = '';
    messages.scrollTop = messages.scrollHeight;
});