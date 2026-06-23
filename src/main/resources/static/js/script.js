document.addEventListener("DOMContentLoaded", function () {
	const rowsPerPage = 5;
	let currentPage = 1;
	let filteredRows = [];
	
	const tableBody = document.getElementById("postsTableBody");
	const allRows = Array.from(tableBody.querySelectorAll("tr"));
	const searchInput = document.getElementById("searchInput");
	
	filteredRows = [...allRows];
	
	function renderTable() {
		const start = (currentPage - 1) * rowsPerPage;
		const end = start + rowsPerPage;
		
		allRows.forEach(row => row.style.display = "none");
		
		filteredRows.forEach((row, index) => {
			row.style.display = (index >= start && index < end) ? "" : "none";
		});
		
		updateInfo(start, end);
		updateButtons();
	}
	
	function updateInfo(start, end) {
		const total = filteredRows.length;
		if (total === 0) {
			document.getElementById("pageInfo").innerHTML = `No results found`;
			return;
		}
		document.getElementById("pageInfo").innerHTML =
			`Showing <b>${start + 1}</b> to <b>${Math.min(end, total)}</b> of <b>${total}</b> entries`;
	}
	
	function updateButtons() {
		const prevBtn = document.getElementById("prevBtn");
		const nextBtn = document.getElementById("nextBtn");
		const totalPages = Math.ceil(filteredRows.length / rowsPerPage);
		
		const isFirstPage = currentPage === 1;
		const isLastPage = currentPage >= totalPages;
		
		prevBtn.disabled = isFirstPage;
		nextBtn.disabled = isLastPage;
		
		prevBtn.classList.toggle("disabled", isFirstPage);
		nextBtn.classList.toggle("disabled", isLastPage);
	}
	
	function nextPage() {
		const totalPages = Math.ceil(filteredRows.length / rowsPerPage);
		if (currentPage < totalPages) {
			currentPage++;
			renderTable();
		}
	}
	
	function prevPage() {
		if (currentPage > 1) {
			currentPage--;
			renderTable();
		}
	}
	
	searchInput.addEventListener("input", function () {
		const query = this.value.trim().toLowerCase();
		
		filteredRows = allRows.filter(row => {
			const messageCell = row.querySelector("td:first-child div");
			const message = (messageCell?.textContent || "").toLowerCase();
			return message.includes(query);
		});
		
		currentPage = 1;
		renderTable();
	});
	
	document.querySelector(".filter-btn").addEventListener("click", function () {
		exportCSV();
	});
	
	function exportCSV() {
		const headers = ["Message", "Created Time", "Reactions", "Comments", "Engagement"];
		
		const rows = allRows.map(row => {
			const cells = row.querySelectorAll("td");
			const messageDiv = cells[0]?.querySelector("div");
			const rawMessage = messageDiv?.getAttribute("title")?.trim()
				|| messageDiv?.textContent
				|| "";
			
			const message = rawMessage.replace(/\r?\n|\r/g, " ").trim();
			
			return [
				message,
				cells[1]?.querySelector("span")?.textContent?.trim() || "",
				cells[2]?.querySelector("span")?.textContent?.trim() || "",
				cells[3]?.querySelector("span")?.textContent?.trim() || "",
				cells[4]?.querySelector("span")?.textContent?.trim() || ""
			];
		});
		
		const csvContent = [headers, ...rows]
			.map(row => row.map(cell => `"${cell.replace(/"/g, '""')}"`).join(","))
			.join("\n");
		
		const blob = new Blob(["\uFEFF" + csvContent], { type: "text/csv;charset=utf-8;" });
		const url = URL.createObjectURL(blob);
		
		const link = document.createElement("a");
		link.href = url;
		link.download = "post_performance_analysis.csv";
		link.click();
		
		URL.revokeObjectURL(url);
	}
	
	window.nextPage = nextPage;
	window.prevPage = prevPage;
	
	renderTable();
});
