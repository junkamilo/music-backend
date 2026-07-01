INSERT INTO featured_artists (spotify_id, stage_name, image_url, spotify_url, popularity, genres, sync_status, last_synced_at)
VALUES
    (
        '0EmeFodog0BfCgMzAIvKQp',
        'Shakira',
        'https://i.scdn.co/image/ab6761610000e5eb17f15f351cba70561ad8bcac',
        'https://open.spotify.com/artist/0EmeFodog0BfCgMzAIvKQp',
        94,
        'reggaeton,latin-pop',
        'SYNCED',
        CURRENT_TIMESTAMP
    ),
    (
        '4q3ewBCX7sLwd24euuV69X',
        'Bad Bunny',
        'https://i.scdn.co/image/ab6761610000e5eb5a00969a4698c313314a57e4',
        'https://open.spotify.com/artist/4q3ewBCX7sLwd24euuV69X',
        98,
        'reggaeton,trap-latino',
        'SYNCED',
        CURRENT_TIMESTAMP
    ),
    (
        '790FomKkXshlbRYZFtlgla',
        'KAROL G',
        'https://i.scdn.co/image/ab6761610000e5eb66041ce9eb4497057cbc3496',
        'https://open.spotify.com/artist/790FomKkXshlbRYZFtlgla',
        92,
        'reggaeton,latin-pop',
        'SYNCED',
        CURRENT_TIMESTAMP
    )
ON CONFLICT (spotify_id) DO NOTHING;

INSERT INTO featured_artists_curated (featured_artist_id, curator_reason, spotlight_order, is_active, featured_from, featured_until)
SELECT fa.id, seed.curator_reason, seed.spotlight_order, TRUE, CURRENT_DATE, CURRENT_DATE + INTERVAL '30 days'
FROM (
    VALUES
        ('0EmeFodog0BfCgMzAIvKQp', 'Trending esta semana', 1),
        ('4q3ewBCX7sLwd24euuV69X', 'Destacado', 2),
        ('790FomKkXshlbRYZFtlgla', 'Descubrimiento', 3)
) AS seed(spotify_id, curator_reason, spotlight_order)
JOIN featured_artists fa ON fa.spotify_id = seed.spotify_id
WHERE NOT EXISTS (
    SELECT 1
    FROM featured_artists_curated fac
    WHERE fac.featured_artist_id = fa.id
      AND fac.spotlight_order = seed.spotlight_order
);
