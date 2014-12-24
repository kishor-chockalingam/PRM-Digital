//
//  JawboneService.m
//  Hybris
//
//  Created by Accenture on 14-12-18.
//  Copyright (c) 2014年 Red Ant. All rights reserved.
//

#import "JawboneService.h"

NSString *const kAPIExplorerID = @"2oscoof9Jn4";
NSString *const kAPIExplorerSecret = @"ab889cb872d35f05f3ae1218b5e9790f6739292f";

@interface JawboneService ()

@property (nonatomic, strong) UPPlatformSessionCompletion sessionCompletion;

@end

@implementation JawboneService

static JawboneService *sharedService = nil;

+ (JawboneService *)sharedService {
    
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedService = [[JawboneService alloc] init];
    });
    return sharedService;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
}

- (void)startSessionCompletion:(UPPlatformSessionCompletion)sessionCompletion {
    
    self.sessionCompletion = sessionCompletion;
    
    [[UPPlatform sharedPlatform] startSessionWithClientID:kAPIExplorerID clientSecret:kAPIExplorerSecret authScope:UPPlatformAuthScopeAll completion:^(UPSession *session, NSError *error) {
        
        if (session) {
            
            self.sessionCompletion(session, nil);
        }else {
            self.sessionCompletion(nil, error);
        }
    }];
}

- (void)endCurrentSession {
    
    [[UPPlatform sharedPlatform] endCurrentSession];
    
    for (NSHTTPCookie *cookie in [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookiesForURL:[NSURL URLWithString:[UPPlatform basePlatformURL]]])
    {
        [[NSHTTPCookieStorage sharedHTTPCookieStorage] deleteCookie:cookie];
    }
}

- (void)validateSessionWithCompletion:(UPPlatformSessionCompletion)sessionCompletion {
    
    self.sessionCompletion = sessionCompletion;
    
    [[UPPlatform sharedPlatform] validateSessionWithCompletion:^(UPSession *session, NSError *error) {
        
        if (session) {
            
            self.sessionCompletion(session, nil);
        }else {
            self.sessionCompletion(nil, error);
        }
    }];
}

@end
