import React, { useEffect } from 'react';
import { Button } from 'react-bootstrap';

const Wallpaper = ({ imageUrl, existingLink, wallpaperIsNull, role }) => {

  useEffect(() => {
    console.log("hallofrawallpaper")
    console.log(imageUrl)
    console.log(existingLink)
    console.log(wallpaperIsNull)
    console.log(role)
    return () => {

    }
  }, [])
  


  const wallpaperStyle = {
    backgroundSize: wallpaperIsNull ? 'cover' : '100%', // Adjusted backgroundSize
    backgroundPosition: 'center',
    width: '100%',
    height: wallpaperIsNull ? '600px' : '600px', // Adjusted height
    backgroundColor: 'lightgrey',
    ...(wallpaperIsNull ? {} : { backgroundImage: `url('${imageUrl}')`, height: '600px' }),
  };

  const setWallpaperLink = `${existingLink}/set_wallpaper`;

  return (
    <div style={wallpaperStyle}>
      {/* Conditionally render the button only if the user is a moderator or admin */}
        <Button href={setWallpaperLink} className="btn btn-link" style={{ color: 'inherit', textDecoration: 'none', transition: 'color 0.3s ease' }}>
          Set wallpaper
        </Button>
    </div>
  );
};

export default Wallpaper;



