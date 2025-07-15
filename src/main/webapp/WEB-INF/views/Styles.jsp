<style>
    body {
        margin: 0;
        padding: 0;
        font-family: Arial, sans-serif;
        height: 100vh;
        background-color: #f5f0ff;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .form-container {
        background-color: white;
        padding: 30px;
        border-radius: 16px;
        width: 100%;
        max-width: 400px;
        box-shadow: 0 0 30px rgba(0, 0, 0, 0.08);
    }

    h2 {
        text-align: center;
        color: #8B5CF6;
        margin-bottom: 20px;
    }

    .input-container {
        position: relative;
        margin-bottom: 20px;
    }

    .input-container input {
        width: 100%;
        padding: 14px 10px;
        font-size: 10px;
        border: 1px solid #ccc;
        border-radius: 8px;
        outline: none;
    }

    .input-container label {
        position: absolute;
        top: 50%;
        left: 12px;
        transform: translateY(-50%);
        background-color: white;
        padding: 0 4px;
        color: #999;
        pointer-events: none;
        transition: 0.2s ease all;
    }

    .input-container input:focus + label,
    .input-container input:not(:placeholder-shown) + label {
        top: -8px;
        left: 10px;
        font-size: 8px;
        color: #8B5CF6;
    }
    
    label {
    font-size: 8px; 
    color: #bbb;     
	}

    .btn {
        width: 100%;
        padding: 12px;
        border: none;
        border-radius: 8px;
        color: white;
        background-color: #8B5CF6;
        font-weight: bold;
        margin-top: 10px;
        cursor: pointer;
    }

    .btn-reset {
        background-color: #fca5a5;
        color: #6b21a8;
    }

    .link {
        text-align: center;
        margin-top: 15px;
    }

    .link a {
        color: #6d28d9;
        text-decoration: none;
        font-weight: bold;
    }
</style>
