import React from 'react';
import { Button, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const RoundImage = ({ imageUrl, to, sizeInPixels, text }) => {
  return (
    <div
      className="position-relative rounded-circle overflow-hidden"
      style={{
        width: sizeInPixels,
        height: sizeInPixels,
        overflow: 'hidden',
        border: '2px solid white', // Add white border
      }}
    >
      <img
        src={imageUrl}
        alt="Profile"
        className="img-fluid rounded-circle"
        style={{ width: '100%', height: '100%', objectFit: 'cover' }}
      />




      <OverlayTrigger
        placement="bottom"
        overlay={<Tooltip id="tooltip">{text}</Tooltip>}
      >

        
        <Link to={to} style={{ textDecoration: 'none' }}>
          <Button
            variant="light"
            className="position-absolute top-50 start-50 translate-middle"
            style={{
              backgroundColor: 'white',
              border: '1px solid #ccc',
              padding: '5px',
              borderRadius: '50%',
              transform: 'translate(-50%, -50%)',
              opacity: 0,
            }}
          >
            +
          </Button>
        </Link>
      </OverlayTrigger>
      <style>
        {`
          .rounded-circle:hover button {
            opacity: 1;
          }
        `}
      </style>
    </div>
  );
};

export default RoundImage;
