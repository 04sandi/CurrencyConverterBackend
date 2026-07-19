async function register()
{
 const data={
                name: document.getElementById("name").value,
                email: document.getElementById("email").value,
                password: document.getElementById("password").value
 };

 const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if(!emailPattern.test(data.email))
  {
      document.getElementById("message").innerText =
          "Please enter a valid email address";
      return;
  }

  if(data.password.length < 6)
  {
      document.getElementById("message").innerText =
          "Password must be at least 6 characters";
      return;
  }

 const response= await fetch("/register",{
            method: "POST",
            headers:{
              "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
 });
 const result= await response.text();
 document.getElementById("message").innerText = result;
 }

 async function login() {

     const data = {
         email: document.getElementById("email").value,
         password: document.getElementById("password").value
     };

     const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

         if(!emailPattern.test(data.email))
         {
             document.getElementById("loginMessage").innerText =
                 "Please enter a valid email address";
             return;
         }


     const response = await fetch("/login", {
         method: "POST",
         headers: {
             "Content-Type": "application/json"
         },
         body: JSON.stringify(data)
     });

    const result = await response.json();

    if(response.ok)
    {
     console.log(result);

     localStorage.setItem("apiKey", result.apiKey);

     window.location.href = "dashboard.html";
     }
     else
     {
        document.getElementById("loginMessage").innerText=result.message;
     }
 }

 async function convertCurrency() {

     const apiKey = localStorage.getItem("apiKey");

     const data = {
         from: document.getElementById("from").value,
         to: document.getElementById("to").value,
         amount: document.getElementById("amount").value
     };

     if(!data.from || !data.to || !data.amount)
     {
           document.getElementById("result").innerText= "please fill all fields";
           return;
     }

     const amount = Number(data.amount)

     if(amount<=0)
     {
            document.getElementById("result").innerText = "Amount must be greater than zero";
            return;
     }

    const btn= document.getElementById("convertBtn");
    btn.innerText = "Converting....";
    btn.disabled = true;


    try{
     const response = await fetch("/convert", {
         method: "POST",
         headers: {
             "Content-Type": "application/json",
             "api-key": apiKey
         },
         body: JSON.stringify(data)
     });

     if (!response.ok) {
                 const errorData = await response.json();
                 throw new Error(errorData.message || "Request failed");
             }


     const result = await response.json();

     document.getElementById("result").innerText =
         result.amount + " " +
         result.fromCurrency + " = " +
         result.convertedAmount + " " +
         result.toCurrency;
       }catch(error)
       {
            document.getElementById("result").innerText =
                    "Error: " + error.message;
       }finally{
     btn.innerText = "Convert";
     btn.disabled = false;
     }
 }

 async function loadCurrencyDropdowns() {

   const response = await fetch("/currencies.json");
       const currencies = await response.json();

       const from = document.getElementById("from");
       const to = document.getElementById("to");

       from.innerHTML = "";
       to.innerHTML = "";

       currencies.forEach(currency => {

           const option1 = document.createElement("option");
           option1.value = currency.code;
           option1.textContent = currency.country;

           const option2 = document.createElement("option");
           option2.value = currency.code;
           option2.textContent = currency.country;

           from.appendChild(option1);
           to.appendChild(option2);

       });

       from.value = "USD";
       to.value = "INR";

       new TomSelect("#from", {
           create: false,
           searchField: "text",
           sortField: {
               field: "text",
               direction: "asc"
           }
       });

       new TomSelect("#to", {
           create: false,
           searchField: "text",
           sortField: {
               field: "text",
               direction: "asc"
           }
       });
 }

 function swapCurrencies()
 {
     const from = document.getElementById("from");
     const to = document.getElementById("to");

     const temp = from.value;

     from.value = to.value;

     to.value = temp;
 }

 async function loadHistory() {

     const apiKey = localStorage.getItem("apiKey");

     const response = await fetch("/history", {
         headers: {
             "api-key": apiKey
         }
     });

     const data = await response.json();

     console.log(data);

     let rows = "";

     data.forEach(item => {

     const date = new Date(item.conversionTime);

             const formattedDate =
                 date.toLocaleDateString("en-GB", {
                     day: "2-digit",
                     month: "short",
                     year: "numeric"
                 }) +
                 " " +
                 date.toLocaleTimeString("en-US", {
                     hour: "2-digit",
                     minute: "2-digit",
                     hour12: true
                 });

         rows += `
         <tr>
             <td>${item.fromCurrency}</td>
             <td>${item.toCurrency}</td>
             <td>${item.amount}</td>
             <td>${item.convertedAmount}</td>
             <td>${formattedDate}</td>
         </tr>
         `;
     });

     document.getElementById("historyBody").innerHTML = rows;
 }

async function loadCurrencies() {

    const response = await fetch("/currencies");
    const data = await response.json();

    let html = "";

    for (let code in data.rates) {
        html += `<li>${code}</li>`;
    }

    document.getElementById("currencyList").innerHTML = html;
}

 function logout() {
     localStorage.removeItem("apiKey");
     window.location.href = "login.html";
 }

 function checkLogin()
 {
    const apiKey = localStorage.getItem("apiKey");

    if(!apiKey)
    {
        window.location.href= "login.html";
    }
 }

 async function deleteHistory() {

     const apiKey = localStorage.getItem("apiKey");

     if(!confirm("Delete all history?"))
     {
         return;
     }

     const response = await fetch("/history", {
         method: "DELETE",
         headers: {
             "api-key": apiKey
         }
     });

     const result = await response.text();

     alert(result);

     loadHistory();
 }