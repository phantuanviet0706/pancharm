<?php

namespace App\Http\Middleware;

use App\Models\Company;
use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;

class InitCompanyIfNotExists
{
    /**
     * Handle an incoming request.
     *
     * @param  \Closure(\Illuminate\Http\Request): (\Symfony\Component\HttpFoundation\Response)  $next
     */
    public function handle(Request $request, Closure $next): Response
    {
        // dd(Company::exists());
        // if (!Company::exists()) {
        //     Company::firstOrCreate(
        //         [
        //             'name' => 'Pancharm',
        //         ]
        //     );
        // }
        return $next($request);
    }
}
