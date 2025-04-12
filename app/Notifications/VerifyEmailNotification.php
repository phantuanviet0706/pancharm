<?php

namespace App\Notifications;

use App\Shared\Translator;
use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Notifications\Messages\MailMessage;
use Illuminate\Notifications\Notification;

class VerifyEmailNotification extends Notification
{
    use Queueable;

    /**
     * Create a new notification instance.
     */
    public function __construct()
    {
        //
    }

    /**
     * Get the notification's delivery channels.
     *
     * @return array<int, string>
     */
    public function via(object $notifiable): array
    {
        return ['mail'];
    }

    /**
     * Get the mail representation of the notification.
     */
    public function toMail(object $notifiable): MailMessage
    {
        $verify_url = url('/verify-email?token=' . $notifiable->verification_token);
        return (new MailMessage)
            ->subject(Translator::trans('Verify Your Email Address'))
            ->greeting(Translator::trans("Hello") . ", {$notifiable->fullname}")
            ->line(Translator::trans('Please click the button below to verify your email address.'))
            ->action(Translator::trans('Verify Email Address'), $verify_url)
            ->line(Translator::trans('If you did not create an account, no further action is required.'))
            ->line(Translator::trans('Thank you for using our application!'))
            ->salutation(Translator::trans('Best regards, ') . config('app.name'));
    }

    /**
     * Get the array representation of the notification.
     *
     * @return array<string, mixed>
     */
    public function toArray(object $notifiable): array
    {
        return [
            //
        ];
    }
}
