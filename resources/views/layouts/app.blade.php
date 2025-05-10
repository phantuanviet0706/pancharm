<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>@yield('title', 'Lavesta')</title>
    <link rel="stylesheet" href="{{ asset('css/home.css') }}">

    <!-- Thêm các link CSS khác nếu cần -->
</head>

<body>
    @include('partials.header')

    <main>
        @yield('content')
    </main>

    @include('partials.footer')

    <script src="{{ asset('js/app.js') }}"></script>
    <!-- Thêm các script JS khác nếu cần -->
</body>

</html>