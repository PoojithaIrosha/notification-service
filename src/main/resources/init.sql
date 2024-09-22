insert into public.users (id, name, password, email, token, verified)
values (6, 'Poojitha Irosha', '123', 'poojithairosha9311@gmail.com', null, true);

insert into public.notification (id, name)
values (1, 'email-verification'),
       (2, 'forgot-password');

insert into public.notification_mode (max_retry_attempts, id, notification_id, notification_type, template_url, subject)
values (3, 2, 2, 'EMAIL', 'forgot-password', 'Reset Password'),
       (3, 1, 1, 'EMAIL', 'email-verification', 'Email Verification'),
       (3, 4, 2, 'WHATSAPP', 'forgot-password-sms', null),
       (3, 3, 2, 'SMS', 'forgot-password-sms', null);

