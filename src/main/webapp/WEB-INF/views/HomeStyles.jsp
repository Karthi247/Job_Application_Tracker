<style>
        body {
            margin: 0;
            font-family: 'Segoe UI', sans-serif;
            background-color: #f6f1ff;
        }

        nav {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 60px;
            background-color: white;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
        }

        .logo {
            display: flex;
            align-items: center;
            font-size: 24px;
            font-weight: bold;
            color: #6c5ce7;
        }

        .logo img {
            height: 32px;
            margin-right: 10px;
        }

        .nav-links {
            display: flex;
            gap: 25px;
            align-items: center;
        }

        .nav-links a {
            text-decoration: none;
            color: #2d3436;
            font-weight: 500;
        }

        .nav-links a.login, .nav-links a.signup {
            background-color: #6c5ce7;
            color: white;
            padding: 8px 16px;
            border-radius: 6px;
        }

        .nav-links form {
            display: inline;
        }

        .nav-links .logout-btn {
            background-color: #e74c3c;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }

        .hero {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 60px 100px;
        }

        .hero-text {
            max-width: 50%;
        }

        .hero-text h1 {
            font-size: 52px;
            color: #6c5ce7;
            margin: 0;
        }

        .hero-text h2 {
            font-size: 48px;
            color: #2d3436;
            margin: 5px 0;
        }

        .hero-text p {
            font-size: 18px;
            color: #555;
            margin: 20px 0 30px;
        }

        .get-started-btn {
            background-color: #6c5ce7;
            color: white;
            padding: 14px 30px;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            text-decoration: none;
        }

        .hero-image img {
            width: 480px;
            max-width: 100%;
        }
    </style>